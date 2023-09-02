package lets.play.slotsjackpotcity.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CheckLink(var url : String = "") {
    private val defaultUrl = "https://gist.githubusercontent.com/andreyKovalskji/d1ee2379182206603f8e485528d4ccde/raw/SlotsJackpotCity"
    init {
        if(url == "") {
            url = defaultUrl
        }
    }
    fun check(context : Context, callback: (JSONObject) -> Unit) {
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            JSONObject(),
            callback, null)
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
}