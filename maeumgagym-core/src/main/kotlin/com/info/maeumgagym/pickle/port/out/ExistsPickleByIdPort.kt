package com.info.maeumgagym.pickle.port.out

interface ExistsPickleByIdPort {
    fun existsPickleById(videoId: String): Boolean
}
