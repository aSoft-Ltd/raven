import kommander.expect
import kotlin.test.Test

class Redundant {
    @Test
    fun should_work() {
        expect(1 + 1).toBe(2)
    }
}