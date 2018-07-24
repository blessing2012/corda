package net.corda.djvm.rules.implementation

import net.corda.djvm.code.Emitter
import net.corda.djvm.code.EmitterContext
import net.corda.djvm.code.Instruction
import org.objectweb.asm.Opcodes.*

/**
 * Use exact integer and long arithmetic where possible.
 */
@Suppress("unused")
class AlwaysUseExactMath : Emitter {

    override fun emit(context: EmitterContext, instruction: Instruction) = context.emit {
        when (instruction.operation) {
            IADD -> {
                invokeStatic("java/lang/Math", "addExact", "(II)I")
                preventDefault()
            }
            LADD -> {
                invokeStatic("java/lang/Math", "addExact", "(JJ)J")
                preventDefault()
            }
            IMUL -> {
                invokeStatic("java/lang/Math", "multiplyExact", "(II)I")
                preventDefault()
            }
            LMUL -> {
                invokeStatic("java/lang/Math", "multiplyExact", "(JJ)J")
                preventDefault()
            }
        }
        // TODO Add mappings for other operations, e.g., increment, negate, etc.
    }

}