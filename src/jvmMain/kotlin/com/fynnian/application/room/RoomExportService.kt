package com.fynnian.application.room

import com.fynnian.application.APIException
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class RoomExportService(
  private val roomRepository: RoomRepository,
  private val answersRepository: AnswersRepository
) {

  fun excelExportRoom(roomCode: String): Workbook {
    val room = roomRepository.getRoomsForManagement(roomCode).firstOrNull()
      ?: throw APIException.NotFound("There is no room with code $roomCode")
    val answers = answersRepository.getAnswersOfRoom(roomCode)

    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("room-$roomCode")

    val roomDataStart = sheet.getNextRowNumber()
    val roomData = listOf(
      "Room Code: ${room.code} - status: ${room.roomStatus}" to null,
      "Title" to room.title,
      "Description" to room.description,
      "Display question" to room.question,
      "Angles / Images" to room.images.size.toString(),
      "Participants" to room.participants.toString(),
      "Total Answers" to room.answers.toString()
    )
    roomData.forEachIndexed { i, value ->
      sheet.createRow(roomDataStart + i).apply {
        createCell(0).apply { setCellValue(value.first) }
        createCell(1).apply { setCellValue(value.second) }
      }
    }

    sheet.addEmptyRow()
    sheet.addEmptyRow()

    // set header for answers
    sheet.createRow(sheet.getNextRowNumber()).apply {
      addCellHead(RoomExportAnswerColumns.USER_ID)
      addCellHead(RoomExportAnswerColumns.ANSWER_NO)
      addCellHead(RoomExportAnswerColumns.ANSWER)
    }

    val answersRowStart = sheet.getNextRowNumber()
    answers.forEachIndexed { i, answer ->
      sheet.createRow(answersRowStart + i).apply {
        addCell(RoomExportAnswerColumns.USER_ID, answer.userId.toString())
        addCell(RoomExportAnswerColumns.ANSWER_NO, answer.no.toString())
        addCell(RoomExportAnswerColumns.ANSWER, answer.answer)
      }
    }

    return workbook
  }

  private fun XSSFSheet.getNextRowNumber() = lastRowNum + 1

  private fun XSSFSheet.addEmptyRow() = this.createRow(getNextRowNumber())

  private fun XSSFRow.addCellHead(column: RoomExportAnswerColumns) {
    addCell(column, column.key)
  }

  private fun XSSFRow.addCell(column: RoomExportAnswerColumns, value: String) {
    createCell(column.index).apply { setCellValue(value) }
  }
}

enum class RoomExportAnswerColumns(val index: Int, val key: String) {
  USER_ID(0, "userId"),
  ANSWER_NO(1, "answerNo"),
  ANSWER(2, "answer"),
  CREATION_DATE(3, "created")
}