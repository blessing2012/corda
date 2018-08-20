package net.corda.djvm.rules.implementation

import net.corda.djvm.code.Emitter
import net.corda.djvm.code.EmitterContext
import net.corda.djvm.code.Instruction
import net.corda.djvm.rules.InstructionRule
import net.corda.djvm.validation.RuleContext
import org.objectweb.asm.Opcodes.*

/**
 * Rule that warns about the use of synchronized code blocks. This class also exposes an emitter that rewrites pertinent
 * monitoring instructions to [POP]'s, as these replacements will remove the object references that [MONITORENTER] and
 * [MONITOREXIT] anticipate to be on the stack.
 */
class IgnoreSynchronizedBlocks : InstructionRule(), Emitter {

    override fun validate(context: RuleContext, instruction: Instruction) = context.validate {
        inform("Stripped monitoring instruction") given (instruction.operation in setOf(MONITORENTER, MONITOREXIT))
    }

    override fun emit(context: EmitterContext, instruction: Instruction) = context.emit {
        when (instruction.operation) {
            MONITORENTER, MONITOREXIT -> {
                // Each of these instructions takes an object reference from the stack as input, so we need to pop
                // that element off to make sure that the stack is balanced.
                pop()
                preventDefault()
            }
        }
    }

}
