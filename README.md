# rest-client
Rest Client in Kotlin for Android.

# usage
```Kotlin
...

CoroutineScope(Dispatchers.IO).launch {
    val url = StringBuilder()
    url.append(server.url).append("/api")

    val params = HashMap<String, Any>()
    params["param1"] = "a"
    params["param2"] = 0

    val token = "xyz"
    val rest = Rest()

    // To Get
    rest.get(url.toString(), params)
    
    // To Get with Bearer token
    rest.get(url.toString(), params, token)

    // To Post
    rest.post(url.toString(), params)
    
    // To Post with Bearer token
    rest.post(url.toString(), params, token)

    // To Put
    val ID = "123"
    rest.put(url.toString(), ID, params)

    // To Put with Bearer token
    val ID = "123"
    rest.put(url.toString(), ID, params, token)
    
    // To Delete
    val ID = "123"
    rest.delete(url.toString(), ID)

    // To Delete with Bearer token
    rest.delete(url.toString(), ID, token)

    // To result in string
    val string = rest.string

    // To result in JSON
    val json = rest.jsonObject

    // To result in JSONArray
    val array = rest.jsonArray
}
```
