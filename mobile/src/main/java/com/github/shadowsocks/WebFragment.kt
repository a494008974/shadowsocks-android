/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.shadowsocks

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import org.simple.eventbus.EventBus
import org.simple.eventbus.Subscriber


class WebFragment : ToolbarFragment() {
    private var mWebView: WebView? = null

    companion object {
        fun newInstance(webUrl: String): WebFragment {
            val fg = WebFragment()
            val bundle = Bundle()
            bundle.putString("url", webUrl)
            fg.arguments = bundle
            return fg
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.layout_web, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAndSetupView(view);
        EventBus.getDefault().post("<===WebFragment===>","onMainEvent")
    }

    // 初始化对象
    fun initAndSetupView(view: View) {
        var url = arguments?.getString("url")
        val webViewContainer = view.findViewById(R.id.web_container) as FrameLayout
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mWebView = WebView(activity)
        webViewContainer.addView(mWebView, params)
        var webSettings = mWebView!!.settings

        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true// 设置允许访问文件数据
        webSettings.setSupportZoom(true)//支持缩放
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        mWebView!!.setOnKeyListener(OnKeyEvent)
        mWebView!!.setWebViewClient(webClient)
        mWebView!!.loadUrl(url)
    }

    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return false
        }
    }

    private val OnKeyEvent = View.OnKeyListener { v, keyCode, event ->
        val action = event.action
        val webView = v as WebView
        if (KeyEvent.ACTION_DOWN == action && KeyEvent.KEYCODE_BACK == keyCode) {
            if (webView?.canGoBack()) {
                webView.goBack()
                return@OnKeyListener true
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView?.clearCache(true)
        (mWebView?.parent as FrameLayout).removeView(mWebView)
        mWebView?.stopLoading()
        mWebView?.setWebViewClient(null)
        mWebView?.setWebChromeClient(null)
        mWebView?.removeAllViews()
        mWebView?.destroy()
        mWebView = null
    }

    @Subscriber(tag = "onFragmentEvent")
    fun onFragmentEvent(event: String) {
        Log.e("ZZZZ","onFragmentEvent => "+event)
    };

}
