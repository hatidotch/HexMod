package at.petrak.hex.common.casting.operators.math

import at.petrak.hex.api.ConstManaOperator
import at.petrak.hex.api.SpellOperator.Companion.getChecked
import at.petrak.hex.api.SpellOperator.Companion.spellListOf
import at.petrak.hex.common.casting.CastingContext
import at.petrak.hex.common.casting.SpellDatum
import net.minecraft.world.phys.Vec3

object OpConstructVec : ConstManaOperator {
    override val argc = 3
    override fun execute(args: List<SpellDatum<*>>, ctx: CastingContext): List<SpellDatum<*>> {
        val x = args.getChecked<Double>(0)
        val y = args.getChecked<Double>(1)
        val z = args.getChecked<Double>(2)
        return spellListOf(Vec3(x, y, z))
    }
}