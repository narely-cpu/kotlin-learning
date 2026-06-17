//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    var userViewModel: UserViewModel = UserViewModel()
    while (true) {

        println("What do you want to do?")
        println("1. Create User")
        println("2. Read User")
        println("3. Update User")
        println("4. Delete User")
        val choiceInput = readLine()
        val choice = when (choiceInput) {
            "1" -> userViewModel.createUser()
            "2" -> userViewModel.readUser()
            "3" -> userViewModel.create()
            "4" -> userViewModel.create()
            else -> continue
        }
    }
}