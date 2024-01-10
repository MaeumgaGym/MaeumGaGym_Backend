package com.info.maeumgagym.common.exception

enum class ErrorCode(
    val status: Int,
    val message: String
) {
    // Feign
    FEIGN_BAD_REQUEST(400, "Feign Bad Request"),
    FEIGN_UNAUTHORIZED(401, "Feign UnAuthorized"),
    FEIGN_FORBIDDEN(403, "Feign Forbidden"),
    FEIGN_SERVER_ERROR(500, "Feign Server Error"),

    // No Content
    THERE_NO_PICKLES(204, "업로드된 피클이 존재하지 않습니다."),

    // Internal Server Error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ROLE_CONVERTER_ERROR(500, "Role Converter Error"),

    // UnAuthorized
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    UNAUTHORIZED(401, "Un Authorized"),

    // Forbidden
    PERMISSION_DENIED(403, "Permission Denied"),

    // Not Found
    USER_NOT_FOUND(404, "User Not Found"),
    POSE_NOT_FOUND(404, "Pose Not Found"),
    PICKLE_NOT_FOUND(404, "Pickle Not Found"),
    ROUTINE_NOT_FOUND(404, "Routine Not Found"),
    PICKLE_COMMENT_NOT_FOUND(404, "Pickle Comment Not Found"),

    // Bad Request
    ALREADY_WITHDRAWAL_USER(400, "Already Withdrawal User"),
    FILE_TYPE_MISS_MATCHED(400, "File, Type Miss Matched"),
    EXERCISE_LIST_CANNOT_EMPTY(400, "Exercise list cannot empty"),
    PICKLE_MISMATCH(400, "Pickle Mismathce"),

    // Conflict
    DUPLICATED_NICKNAME(409, "Duplicated Nickname"),
    ALREADY_EXIST_USER(409, "Already Exists User"),
    ALREADY_NICKNAME_USER(409, "Already Exists Nickname"),

    // Test
    MISMATCH_QUOTE_AND_QUOTER(500, "Mismatch Quote and Quoter")
}
