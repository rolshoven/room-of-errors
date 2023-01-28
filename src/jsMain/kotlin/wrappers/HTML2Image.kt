@file:JsModule("html-to-image")
@file:JsNonModule

package wrappers

import web.html.HTMLElement
import kotlin.js.Promise

external fun toPng(node: HTMLElement): Promise<String>
