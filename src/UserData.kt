data class UserData(val id: String? = null, val name: String, val email: String, val password: String,
               val type: UserType, val pdmId: String? = null, var active: Boolean = false) {
}