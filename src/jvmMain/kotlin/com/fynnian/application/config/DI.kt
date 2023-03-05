package com.fynnian.application.config

import com.fynnian.application.common.Repository
import com.fynnian.application.room.*
import com.fynnian.application.user.UserRepository

data class DI(
  val config: AppConfig
) {
  val repository: Repository = Repository(config.dataSource)
  val userRepository = UserRepository(config.dataSource)
  val roomRepository = RoomRepository(config.dataSource)
  val answersRepository = AnswersRepository(config.dataSource)
  val usersRoomStatusRepository = UsersRoomStatusRepository(config.dataSource)
  val roomExportService = RoomExportService(roomRepository, answersRepository)
  val roomManagementService = RoomManagementService(config.content, roomRepository)
}
