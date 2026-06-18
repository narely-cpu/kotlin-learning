import java.util.UUID

class UserViewModel() {

    private var id: String? = null
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var type: UserType? = null
    private var pdmId: String? = null
    private var active: Boolean = false
    private var listUsers: MutableList<UserData> = mutableListOf()

    fun createUser() {
        println(" ------ Create User -------")
        val name = addName()
        val email = addEmail()
        val password = addPassword()
        val type = addType()
        var newUser: UserData? = null

        if (type == UserType.COLLABORATOR) {
            val pdmList = this.listUsers.filter { it.type == UserType.PDM }.toMutableList()
            if (pdmList.isEmpty()) {
                println("There is no registered PDM. First, register a PDM.")
            } else {
                println("Which PDM will be associated with the user?")
                val pdmId = searchUserByIndex(pdmList)?.id
                if (pdmId != null) {
                    this.pdmId = pdmId
                    newUser = create(name, email, password, type, pdmId)
                }
            }
        } else if (type == UserType.PDM) {
            newUser = create(name, email, password, type)
        }
        if (newUser != null) {
            println("User is created.")
            listUsers.add(newUser)
        } else {
            println("No user created. Try Again.")
        }
    }

    fun readUser(): UserData? {
        println(" ------ Read User -------")
        if (listUsers.isEmpty()) {
            println("There is no registered Users.")
            return null
        } else {
            println("What is the user do you want to read?")
            return searchUserByIndex(listUsers)
        }
    }

    fun updateUser() {
        println(" ------ Update User -------")
        if (listUsers.isEmpty()) {
            println("There is no registered Users.")
        } else {
            println("What is the user do you want to update?")
            var currentUser = searchUserByIndex(listUsers)
            println("Which user attribute would you like to update?")
            if (currentUser != null) {
                var attributesCurrentUser = mutableMapOf(
                    "name" to currentUser.name,
                    "email" to currentUser.email,
                    "password" to currentUser.password,
                    "type" to currentUser.type,
                    "pdmId" to currentUser.pdmId,
                )

                attributesCurrentUser.mapValues { (key, value) ->
                    println("$key: $value ")
                    println("Do you want to modify this attribute? (0 -> No) (1 -> Yes)")
                    val choiceInput = readln()
                    if (choiceInput == "1") {
                        println("Enter the new value")
                        val newValueInput = readln()
                        if (key == "name" || key == "email" || key == "password" || key == "pdmId") {
                            attributesCurrentUser[key] = newValueInput
                        } else {
                            println("1 -> Collaborator")
                            println("2 -> PDM")
                            println("3 -> Admin")
                            val typeInput = readln()
                            when (typeInput) {
                                "1" -> attributesCurrentUser[key] = UserType.COLLABORATOR
                                "2" -> attributesCurrentUser[key] = UserType.PDM
                                "3" -> attributesCurrentUser[key] = UserType.PDM
                            }
                        }
                    }
                }

                currentUser.apply {
                    name = attributesCurrentUser["name"] as String
                    email = attributesCurrentUser["email"] as String
                    password = attributesCurrentUser["password"] as String
                    type = attributesCurrentUser["type"] as UserType
                    pdmId = attributesCurrentUser["pdmId"] as String?
                }
            }
        }
    }

    private fun addName(): String {
        println("What is the username you want to create?")
        val name = readln()
        this.name = name
        return name
    }

    private fun addEmail(): String {
        println("What is the user's email address?")
        val email = readln()
        this.email = email
        return email
    }

    private fun addPassword(): String {
        println("What is the user's password?")
        val password = readln()
        this.password = password
        return password
    }

    private fun addType(): UserType {
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
        this.type = userType
        return userType
    }

    private fun create(name: String, email: String, password: String, type: UserType, pdmId: String? = null): UserData? {
        this.id = UUID.randomUUID().toString()
        if (!validatePassword(password) || !validateInternalEmail(email)) {
            return null
        } else {
            this.active = true
            val user = UserData(id, name, email,
                password, type,
                pdmId, true
            )
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
        if (password.length < 8) {
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

    private fun searchUserByIndex(listUsers: MutableList<UserData>): UserData? {
        for ((index, value) in listUsers.withIndex()) {
            println("$index -> \"${value.name}\"")
        }
        val userIndexInput = readln()
        if (userIndexInput.toDoubleOrNull() != null) {
            return validateIndexInput(userIndexInput.toInt(), listUsers)
        } else {
            println("Enter the index of a valid user.")
            return null
        }
    }

    private fun validateIndexInput(userIndexInput: Int, listUsers: MutableList<UserData>): UserData? {
        if (userIndexInput >= listUsers.count() || userIndexInput < 0) {
            println("Enter the index of a valid user.")
            return null
        } else {
            val userSelected = listUsers[userIndexInput]
            print("data user: $userSelected")
            return userSelected
        }
    }
}