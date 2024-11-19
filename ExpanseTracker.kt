import java.io.File
import java.time.LocalDate

enum class Category {
    GROCERIES,
    BILLS,
    ENTERTAINMENT,
    TRANSPORT,
    OTHER
}

data class Expense(
        val id: Int,
        val description: String,
        val amount: Double,
        val date: LocalDate,
        val category: Category
)

// Add function
fun addExpense(
        expenses: MutableList<Expense>,
        id: Int,
        description: String,
        amount: Double,
        category: Category
) {
    val expense = Expense(id, description, amount, LocalDate.now(), category)
    expenses.add(expense)
    println("Expense added: $expense")
}

fun listExpenses(expenses: List<Expense>) {
    if (expenses.isEmpty()) {
        println("List is empty")
        return
    }

    println("ID\tDescription\tAmount\tDate\t\tCategory")

    expenses.forEach {
        println("${it.id}\t${it.description}\t\t${it.amount}\t${it.date}\t${it.category}")
    }
}

fun filterExpensesByCategory(expenses: List<Expense>, category: Category): List<Expense> {
    return expenses.filter { it.category == category }
}

fun calculateTotal(expenses: List<Expense>): Double {
    return expenses.sumOf { it.amount }
}

fun saveExpensesToFile(expenses: List<Expense>, filename: String) {
    val file = java.io.File(filename)
    file.writeText(
            expenses.joinToString("\n") {
                "${it.id},${it.description},${it.amount},${it.date},${it.category}"
            }
    )
    println("Expenses saved to $filename")
}

fun loadExpensesFromFile(fileName: String): MutableList<Expense> {
    val file = java.io.File(fileName)
    if (!file.exists()) return mutableListOf()

    return file.readLines()
            .map {
                val parts = it.split(",")
                Expense(
                        id = parts[0].toInt(),
                        description = parts[1],
                        amount = parts[2].toDouble(),
                        date = LocalDate.parse(parts[3]),
                        category = Category.valueOf(parts[4])
                )
            }
            .toMutableList()
}

fun main() {
    val expenses = loadExpensesFromFile("expenses.txt")
    println("Welcome to the Expense Tracker!")
    while (true) {
        println("1. Add Expense\n2. View Expenses\n3. Filter by Category\n4. Save and Exit")
        when (readln().toInt()) {
            1 -> {
                println("Enter description:")
                val description = readln()
                println("Enter amount:")
                val amount = readln().toDouble()
                println("Enter category (GROCERIES, BILLS, ENTERTAINMENT, TRANSPORT, OTHER):")
                val category = Category.valueOf(readln().uppercase())
                addExpense(expenses, expenses.size + 1, description, amount, category)
            }
            2 -> listExpenses(expenses)
            3 -> {
                println("Enter category:")
                val category = Category.valueOf(readln().uppercase())
                val filtered = filterExpensesByCategory(expenses, category)
                listExpenses(filtered)
            }
            4 -> {
                saveExpensesToFile(expenses, "expenses.txt")
                println("Goodbye!")
                break
            }
        }
    }
}
