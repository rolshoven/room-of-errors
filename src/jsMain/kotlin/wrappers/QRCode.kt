@file:JsModule("qrcode.react")
@file:JsNonModule

package wrappers

import react.Props
import react.dom.html.CanvasHTMLAttributes
import react.dom.svg.SVGAttributes
import web.html.HTMLCanvasElement
import web.svg.SVGSVGElement


/**
 * ToDo: docs for params
 * https://github.com/zpao/qrcode.react
 */
external interface ImageSettings {
  var src: String
  var height: Number
  var width: Number
  var excavate: Boolean
  var x: Number
  var y: Number
};

/**
 * ToDo: docs for params
 * https://github.com/zpao/qrcode.react
 */
external interface QRProps : Props {
  var value: String
  var size: Number?
  var level: String?
  var bgColor: String?
  var fgColor: String?
  //var style: CSSProperties?
  var includeMargin: Boolean?
  var imageSettings: ImageSettings?
}

external interface QRCodeSVGProps : QRProps, SVGAttributes<SVGSVGElement>

/**
 * Generate the qr code as a svg element
 */
external val QRCodeSVG: react.FC<QRCodeSVGProps>

external interface QRCodeCanvasProps : QRProps, CanvasHTMLAttributes<HTMLCanvasElement>

/**
 * Generate the qr code as a canvas element
 */
external val QRCodeCanvas: react.FC<QRCodeCanvasProps>
