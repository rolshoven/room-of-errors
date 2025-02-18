package com.fynnian.application.config

import com.fynnian.application.common.Repository
import com.fynnian.application.room.*
import com.fynnian.application.user.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

data class DI(
  val config: AppConfig
) {
  val repository: Repository = Repository(config.dataSource)
  val userRepository = UserRepository(config.dataSource)
  val roomRepository = RoomRepository(config.dataSource)
  val answersRepository = AnswersRepository(config.dataSource)
  val usersRoomStatusRepository = UsersRoomStatusRepository(config.dataSource)
  val groupRepository = GroupRepository(config.dataSource)
  val roomExportService = RoomExportService(roomRepository, answersRepository, usersRoomStatusRepository, groupRepository)
  val roomManagementService = RoomManagementService(config.content, roomRepository)
  val profile = config.profile

  val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = config.supabaseUrl,
    supabaseKey = config.supabaseKey
  ) {
    install(Auth)
  }

}
