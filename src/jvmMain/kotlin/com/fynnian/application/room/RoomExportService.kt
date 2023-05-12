package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.I18n
import com.fynnian.application.common.I18n.TranslationKey.*
import com.fynnian.application.common.Language
import com.fynnian.application.common.Repository.Companion.CH_TZ
import kotlinx.datetime.toJavaInstant
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.time.Duration
import java.time.ZonedDateTime


class RoomExportService(
  private val roomRepository: RoomRepository,
  private val answersRepository: AnswersRepository,
  private val usersRoomStatusRepository: UsersRoomStatusRepository,
  private val groupRepository: GroupRepository
) {

  fun excelExportRoom(roomCode: String, language: Language): Workbook {
    val room = roomRepository.getRoomsForManagement(roomCode).firstOrNull()
      ?: throw APIException.NotFound("There is no room with code $roomCode")
    val user2Answers = answersRepository.getAnswersOfRoom(roomCode)
    val userStates = usersRoomStatusRepository.getUserStatesOfRoom(roomCode)
    val groups = groupRepository.getGroupsOfRoom(roomCode)
    val imagesPostion = room.images.mapIndexed { i, image -> image.id to i + 1 }.toMap()


    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("room-$roomCode")
    // size the columns make each colum 30 chars wide
    RoomExportAnswerColumns.values().forEach {
      sheet.setColumnWidth(it.index, 30 * 256)
    }
    // simple styling for numbers to prevent the number as text warning in excel
    val numberCellStyle = workbook.createCellStyle().apply {
      dataFormat = workbook.createDataFormat().getFormat("#")
    }


    val roomDataStart = sheet.getNextRowNumber()
    val roomData = listOf(
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_ROOM_CODE) to room.code,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_TITLE) to room.title,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_DESCRIPTION) to room.description,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_QUESTION) to room.question,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_IMAGES) to room.images.size,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_COLLECT_GROUP_INFO) to room.withGroupInformation,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_GROUPS) to room.groups,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS) to room.participants,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_TOTAL_ANSWERS) to room.answers,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS_WITHOUT_ANSWERS) to userStates.filterKeys { user2Answers[it] == null }.size
    )
    roomData.forEachIndexed { i, (text, value) ->
      sheet.createRow(roomDataStart + i).apply {
        addCell(0, text)
        when (value) {
          is Int -> addCell(1, value, numberCellStyle)
          else -> addCell(1, value ?: "")
        }
      }
    }

    sheet.addEmptyRow()
    sheet.addEmptyRow()

    // set header for answers
    sheet.createRow(sheet.getNextRowNumber()).apply {
      addCellHead(RoomExportAnswerColumns.USER_NO, language)
      addCellHead(RoomExportAnswerColumns.ANSWER_NO, language)
      addCellHead(RoomExportAnswerColumns.IMAGE_NO, language)
      addCellHead(RoomExportAnswerColumns.ANSWER, language)
      addCellHead(RoomExportAnswerColumns.GROUP_NAME, language)
      addCellHead(RoomExportAnswerColumns.GROUP_SIZE, language)
      addCellHead(RoomExportAnswerColumns.STARTED, language)
      addCellHead(RoomExportAnswerColumns.FINISHED, language)
      addCellHead(RoomExportAnswerColumns.TIME, language)
    }

    var currentAnswerRow = sheet.getNextRowNumber()
    user2Answers.entries.forEachIndexed { userNo, (_, answers) ->
      answers.forEachIndexed { i, answer ->
        sheet.createRow(currentAnswerRow).apply {
          addCell(RoomExportAnswerColumns.USER_NO, userNo + 1, numberCellStyle)
          addCell(RoomExportAnswerColumns.ANSWER_NO, answer.no, numberCellStyle)
          addCell(RoomExportAnswerColumns.IMAGE_NO, imagesPostion[answer.imageId] ?: 0, numberCellStyle)
          addCell(RoomExportAnswerColumns.ANSWER, answer.answer)
          addCell(RoomExportAnswerColumns.GROUP_NAME, groups[answer.userId]?.groupName ?: "")
          addCell(RoomExportAnswerColumns.GROUP_SIZE, groups[answer.userId]?.groupSize ?: 0, numberCellStyle)

          val started = userStates[answer.userId]?.startedAt?.let { ZonedDateTime.ofInstant(it.toJavaInstant(), CH_TZ) }
          val finished =
            userStates[answer.userId]?.finishedAt?.let { ZonedDateTime.ofInstant(it.toJavaInstant(), CH_TZ) }

          addCell(RoomExportAnswerColumns.STARTED, started ?: "")
          addCell(RoomExportAnswerColumns.FINISHED, finished ?: "")
          addCell(
            RoomExportAnswerColumns.TIME,
            Duration.between(started, finished)
              .run { "%02d:%02d:%02d".format(toHours(), toMinutesPart(), toSecondsPart()) })


          currentAnswerRow++
        }
      }
    }

    return workbook
  }

  private fun XSSFSheet.getNextRowNumber() = lastRowNum + 1

  private fun XSSFSheet.addEmptyRow() = this.createRow(getNextRowNumber())

  private fun XSSFRow.addCellHead(column: RoomExportAnswerColumns, language: Language) {
    addCell(column, column.getKeyText(language))
  }

  private fun XSSFRow.addCell(column: RoomExportAnswerColumns, value: Any) {
    addCell(column.index, value)
  }

  private fun XSSFRow.addCell(index: Int, value: Any) {
    createCell(index).apply {
      setCellValue(value.toString())
    }
  }

  private fun XSSFRow.addCell(column: RoomExportAnswerColumns, value: Int, style: XSSFCellStyle) {
    addCell(column.index, value, style)
  }


  private fun XSSFRow.addCell(index: Int, value: Int, style: XSSFCellStyle) {
    createCell(index).apply {
      setCellValue(value.toDouble())
      cellStyle = style
    }
  }
}

enum class RoomExportAnswerColumns(val index: Int, val key: I18n.TranslationKey) {
  USER_NO(0, ROOM_EXCEL_EXPORT_HEADER_USER_NO),
  GROUP_NAME(1, ROOM_EXCEL_EXPORT_HEADER_GROUP_NAME),
  GROUP_SIZE(2, ROOM_EXCEL_EXPORT_HEADER_GROUP_SIZE),
  STARTED(3, ROOM_EXCEL_EXPORT_HEADER_STARTED),
  FINISHED(4, ROOM_EXCEL_EXPORT_HEADER_FINISHED),
  TIME(5, ROOM_EXCEL_EXPORT_HEADER_TIME),
  ANSWER_NO(6, ROOM_EXCEL_EXPORT_HEADER_ANSWER_NO),
  IMAGE_NO(7, ROOM_EXCEL_EXPORT_HEADER_IMAGE_NO),
  ANSWER(8, ROOM_EXCEL_EXPORT_HEADER_ANSWER),
  ;

  fun getKeyText(language: Language) = I18n.get(language, key)
}