import java.util.UUID

class UserViewModel() {

    private var id: String? = null
    private val name: String? = null
    private val email: String? = null
    private val password: String? = null
    private val type: UserType? = null
    private val pdmId: String? = null
    private var active: Boolean = false
    private var listUsers: MutableList<UserData> = mutableListOf()

    fun createUser() {
        println(" ------ Create User -------")
        println("What is the username you want to create?")
        val name = readln()
        println("What is the user's email address?")
        val email = readln()
        println("What is the user's password?")
        val password = readln()
        println("What type of user do you have?")
        println("1. PDM")
        println("2. COLLABORATOR")
        println("3. ADMIN")
        val userTypeInput = readln()
        val userType = when (userTypeInput) {
            "1" -> UserType.PDM
            "2" -> UserType.COLLABORATOR
            "3" -> UserType.ADMIN
            else -> UserType.COLLABORATOR
        }
        var newUser: UserData? = null
        if (userType == UserType.COLLABORATOR) {
            val pdmList = this.listUsers.filter { it.type == UserType.PDM }
            if (pdmList.isEmpty()) {
                println("There is no registered PDM. First, register a PDM.")
            } else {
                println("Which PDM will be associated with the user?")
                for ((index, value) in pdmList.withIndex()) {
                    println("$index -> \"${value.email}\"")
                }
                val pdmIndexInput = readln()
                val pdmId = listUsers[pdmIndexInput.toInt()].id
                newUser = create()
            }
        } else if (userType == UserType.PDM) {
            newUser = create()
        }
        if (newUser != null) {
            println("User is now created.")
            println(newUser)
            listUsers.add(newUser)
        } else {
            println("No user created. Try Again.")
        }
    }

    fun readUser(): UserData? {
        println(" ------ Read User -------")
        for ((index, value) in listUsers.withIndex()) {
            println("$index -> \"${value.name}\"")
        }
        if (listUsers.isEmpty()) {
            println("There is no registered Users.")
            return null
        } else {
            println("What is the user do you want to read?")
            val userIndexInput = readln()
            if (listUsers.count() -1 > userIndexInput.toInt()) {
                val userSelected = listUsers[userIndexInput.toInt()]
                if (userSelected != null) {
                    val userObject = UserData(userSelected.id, userSelected.name, userSelected.email,
                        userSelected.password, userSelected.type,
                        userSelected.pdmId, userSelected.active)
                    print("data user: $userObject")
                    return userObject
                } else {
                    println("No found user. Try Again.")
                    return null
                }
            } else {
                println("There is no registered Users.")
                return null
            }

        }
    }

    fun create(): UserData? {
        this.id = UUID.randomUUID().toString()
        if (!validatePassword(this.password) || !validateInternalEmail(this.email)) {
            return null
        } else {
            this.active = true
            val user = UserData(this.id, this.name, this.email,
                this.password, this.type,
                this.pdmId, this.active = true)
            return user
        }
    }

    private fun validateInternalEmail(email: String): Boolean {
        val internalDomains: List<String> = listOf("ciandt.com", "ciandt.com.br")
        if (!email.contains("@")) {
            println("Invalid email")
            return false
        }
        val domain = email.substring(email.indexOf("@") + 1).lowercase()
        if (!internalDomains.contains(domain)) {
            println("Only CI&T corporate emails (@ciandt.com) are allowed for internal users")
            return false
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password == null || password.length < 8) {
            println("Password must be at least 8 characters long")
            return false
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            println("Password must contain at least one uppercase letter")
            return false
        }
        if (!password.matches(".*\\d.*".toRegex())) {
            println("Password must contain at least one number")
            return false
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-={}:;\"',.<>/?].*".toRegex())) {
            println("Password must contain at least one special character")
            return false
        }
        return true
    }
}