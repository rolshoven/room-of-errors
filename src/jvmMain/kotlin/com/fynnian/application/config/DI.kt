package com.fynnian.application.config

import com.fynnian.application.common.Repository
import com.fynnian.application.room.AnswersRepository
import com.fynnian.application.room.RoomExportService
import com.fynnian.application.room.RoomRepository
import com.fynnian.application.user.UserRepository

data class DI(
  val config: AppConfig
) {
  val repository: Repository = Repository(config.dataSource)
  val userRepository = UserRepository(config.dataSource)
  val roomRepository = RoomRepository(config.dataSource)
  val answersRepository = AnswersRepository(config.dataSource)
  val roomExportService = RoomExportService(roomRepository, answersRepository)
}
