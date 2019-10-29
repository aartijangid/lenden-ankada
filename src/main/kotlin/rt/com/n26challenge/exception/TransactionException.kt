package rt.com.n26challenge.exception

class TransactionException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
