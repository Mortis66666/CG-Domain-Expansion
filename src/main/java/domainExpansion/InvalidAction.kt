package domainExpansion

class InvalidAction(message: String?) : Exception(message) {
    companion object {
        private const val serialVersionUID = -8185589153224401564L
    }
}