package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.I18n
import com.fynnian.application.common.I18n.TranslationKey.*
import com.fynnian.application.common.Language
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class RoomExportService(
  private val roomRepository: RoomRepository,
  private val answersRepository: AnswersRepository
) {

  fun excelExportRoom(roomCode: String, language: Language): Workbook {
    val room = roomRepository.getRoomsForManagement(roomCode).firstOrNull()
      ?: throw APIException.NotFound("There is no room with code $roomCode")
    val user2Answers = answersRepository.getAnswersOfRoom(roomCode)
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
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS) to room.participants,
      I18n.get(language, ROOM_EXCEL_EXPORT_INFO_CARD_TOTAL_ANSWERS) to room.answers
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
    }

    var currentAnswerRow = sheet.getNextRowNumber()
    user2Answers.entries.forEachIndexed { userNo, (_, answers) ->
      answers.forEachIndexed { i, answer ->
        sheet.createRow(currentAnswerRow).apply {
          addCell(RoomExportAnswerColumns.USER_NO, userNo + 1, numberCellStyle)
          addCell(RoomExportAnswerColumns.ANSWER_NO, answer.no, numberCellStyle)
          addCell(RoomExportAnswerColumns.IMAGE_NO, imagesPostion[answer.imageId] ?: 0, numberCellStyle)
          addCell(RoomExportAnswerColumns.ANSWER, answer.answer)
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
  ANSWER_NO(1, ROOM_EXCEL_EXPORT_HEADER_ANSWER_NO),
  IMAGE_NO(2, ROOM_EXCEL_EXPORT_HEADER_IMAGE_NO),
  ANSWER(3, ROOM_EXCEL_EXPORT_HEADER_ANSWER);

  fun getKeyText(language: Language) = I18n.get(language, key)
}