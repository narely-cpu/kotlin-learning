data class UserData(val id: String? = null, var name: String, var email: String, var password: String,
               var type: UserType, var pdmId: String? = null, var active: Boolean = false) {
}