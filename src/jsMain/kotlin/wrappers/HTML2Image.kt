@file:JsModule("html-to-image")
@file:JsNonModule

package wrappers

import js.typedarrays.Uint8ClampedArray
import org.w3c.fetch.RequestInit
import web.buffer.Blob
import web.html.HTMLElement
import kotlin.js.Promise

/**
 * Options for the html-to-image functions
 *
 * [Options](https://github.com/bubkoo/html-to-image#options)
 *
 * Generated with dukat
 */
external interface Options {
  /**
   * Width in pixels to be applied to node before rendering.
   */
  var width: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Height in pixels to be applied to node before rendering.
   */
  var height: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A string value for the background color, any valid CSS color value.
   */
  var backgroundColor: String?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Width in pixels to be applied to canvas on export.
   */
  var canvasWidth: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Height in pixels to be applied to canvas on export.
   */
  var canvasHeight: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * An object whose properties to be copied to node's style before rendering.
   */
  var style: Any?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A function taking DOM node as argument. Should return `true` if passed
   * node should be included in the output. Excluding node means excluding
   * it's children as well.
   */
  var filter: ((domNode: org.w3c.dom.HTMLElement) -> Boolean)?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A number between `0` and `1` indicating image quality (e.g. 0.92 => 92%)
   * of the JPEG image.
   */
  var quality: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Set to `true` to append the current time as a query string to URL
   * requests to enable cache busting.
   */
  var cacheBust: Boolean?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Set false to use all URL as cache key.
   * Default: false | undefined - which strips away the query parameters
   */
  var includeQueryParams: Boolean?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A data URL for a placeholder image that will be used when fetching
   * an image fails. Defaults to an empty string and will render empty
   * areas for failed images.
   */
  var imagePlaceholder: String?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * The pixel ratio of captured image. Defalut is the actual pixel ratio of
   * the device. Set 1 to use as initial-scale 1 for the image
   */
  var pixelRatio: Number?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * Option to skip the fonts download and embed.
   */
  var skipFonts: Boolean?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * The preferred font format. If specified all other font formats are ignored.
   * 'woff' | 'woff2' | 'truetype' | 'opentype' | 'embedded-opentype' | 'svg'
   */
  var preferredFontFormat: String? /* "woff" | "woff2" | "truetype" | "opentype" | "embedded-opentype" | "svg" | String? */
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A CSS string to specify for font embeds. If specified only this CSS will
   * be present in the resulting image. Use with `getFontEmbedCSS()` to
   * create embed CSS for use across multiple calls to library functions.
   */
  var fontEmbedCSS: String?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A boolean to turn off auto scaling for truly massive images..
   */
  var skipAutoScale: Boolean?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * A string indicating the image format. The default type is image/png; that type is also used if the given type isn't supported.
   */
  var type: String?
    get() = definedExternally
    set(value) = definedExternally

  /**
   * The second parameter of  window.fetch (Promise<Response> fetch(input[, init]))
   */
  var fetchRequestInit: RequestInit?
    get() = definedExternally
    set(value) = definedExternally
}

external fun toPng(node: HTMLElement, options: Options? = definedExternally): Promise<String>
external fun toSvg(node: HTMLElement, options: Options? = definedExternally): Promise<String>
external fun toJpeg(node: HTMLElement, options: Options? = definedExternally): Promise<String>
external fun toBlob(node: HTMLElement, options: Options? = definedExternally): Promise<Blob?>
external fun getFontEmbedCSS(node: HTMLElement, options: Options? = definedExternally): Promise<String>
external fun toPixelData(node: HTMLElement, options: Options? = definedExternally): Promise<Uint8ClampedArray>
