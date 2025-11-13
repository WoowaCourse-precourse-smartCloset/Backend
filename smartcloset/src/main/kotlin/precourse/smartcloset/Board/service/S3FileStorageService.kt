package precourse.smartcloset.Board.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import precourse.smartcloset.common.util.Constants.FILE_EMPTY_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.FILE_SIZE_EXCEED_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.IMAGE_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.S3_DELETE_FAILED_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.S3_UPLOAD_FAILED_ERROR_MESSAGE
import java.util.*

@Service
class S3FileStorageService(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String
) {
    companion object {
        private val ALLOWED_IMAGE_TYPES = listOf(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
        )
        private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
    }

    fun saveImage(file: MultipartFile): String {
        validateImageFile(file)

        val fileName = generateUniqueFileName(file.originalFilename ?: "image")
        val metadata = createMetadata(file)

        uploadToS3(fileName, file, metadata)

        return getImageUrl(fileName)
    }

    fun deleteImage(imageUrl: String) {
        runCatching {
            val fileName = extractFileNameFromUrl(imageUrl)
            deleteFromS3(fileName)
        }.onFailure {
            throw IllegalStateException(S3_DELETE_FAILED_ERROR_MESSAGE)
        }
    }

    private fun validateImageFile(file: MultipartFile) {
        validateFileNotEmpty(file)
        validateImageFormat(file)
        validateFileSize(file)
    }

    private fun validateFileNotEmpty(file: MultipartFile) {
        require(!file.isEmpty) { FILE_EMPTY_ERROR_MESSAGE }
    }

    private fun validateImageFormat(file: MultipartFile) {
        val contentType = file.contentType
        require(contentType in ALLOWED_IMAGE_TYPES) { IMAGE_FORMAT_ERROR_MESSAGE }
    }

    private fun validateFileSize(file: MultipartFile) {
        require(file.size <= MAX_FILE_SIZE) { FILE_SIZE_EXCEED_ERROR_MESSAGE }
    }

    private fun generateUniqueFileName(originalFilename: String): String {
        val extension = extractFileExtension(originalFilename)
        val uniqueId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()
        return "images/${timestamp}_${uniqueId}.$extension"
    }

    private fun extractFileExtension(filename: String): String {
        return filename.substringAfterLast(".", "jpg")
    }

    private fun createMetadata(file: MultipartFile): ObjectMetadata {
        return ObjectMetadata().apply {
            contentType = file.contentType
            contentLength = file.size
        }
    }

    private fun uploadToS3(fileName: String, file: MultipartFile, metadata: ObjectMetadata) {
        runCatching {
            val putObjectRequest = createPutObjectRequest(fileName, file, metadata)
            amazonS3.putObject(putObjectRequest)
        }.onFailure {
            throw IllegalStateException(S3_UPLOAD_FAILED_ERROR_MESSAGE)
        }
    }

    private fun createPutObjectRequest(
        fileName: String,
        file: MultipartFile,
        metadata: ObjectMetadata
    ): PutObjectRequest {
        return PutObjectRequest(bucket, fileName, file.inputStream, metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)
    }

    private fun deleteFromS3(fileName: String) {
        val deleteRequest = DeleteObjectRequest(bucket, fileName)
        amazonS3.deleteObject(deleteRequest)
    }

    private fun getImageUrl(fileName: String): String {
        return amazonS3.getUrl(bucket, fileName).toString()
    }

    private fun extractFileNameFromUrl(imageUrl: String): String {
        val uri = java.net.URI(imageUrl)
        return uri.path.substring(1)
    }
}