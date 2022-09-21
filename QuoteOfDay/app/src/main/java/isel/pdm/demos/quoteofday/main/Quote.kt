package isel.pdm.demos.quoteofday.main

data class Quote(val content: String, val author: String) {
    init {
        require(content.isNotBlank())
        require(author.isNotBlank())
    }
}