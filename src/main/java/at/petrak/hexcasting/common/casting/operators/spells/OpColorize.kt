package at.petrak.hexcasting.common.casting.operators.spells

import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellDatum
import at.petrak.hexcasting.api.spell.SpellOperator
import at.petrak.hexcasting.common.casting.CastingContext
import at.petrak.hexcasting.common.casting.colors.FrozenColorizer
import at.petrak.hexcasting.common.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.common.lib.HexCapabilities
import at.petrak.hexcasting.common.network.HexMessages
import at.petrak.hexcasting.common.network.MsgColorizerUpdateAck
import net.minecraftforge.network.PacketDistributor

object OpColorize : SpellOperator {
    override val argc = 0

    override fun execute(
        args: List<SpellDatum<*>>,
        ctx: CastingContext
    ): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val handStack = ctx.getHeldItemToOperateOn { FrozenColorizer.isColorizer(it.item) }
        if (!FrozenColorizer.isColorizer(handStack.item)) {
            throw MishapBadOffhandItem.of(
                handStack,
                "colorizer"
            )
        }
        return Triple(
            Spell,
            10_000,
            listOf()
        )
    }

    private object Spell : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            val maybeCap = ctx.caster.getCapability(HexCapabilities.PREFERRED_COLORIZER).resolve()
            if (!maybeCap.isPresent)
                return
            val cap = maybeCap.get()

            val handStack = ctx.getHeldItemToOperateOn { FrozenColorizer.isColorizer(it.item) }
            if (FrozenColorizer.isColorizer(handStack.item)) {
                val item = handStack.item
                if (ctx.withdrawItem(handStack.item, 1, true)) {
                    cap.colorizer = FrozenColorizer(item, ctx.caster.uuid)
                    HexMessages.getNetwork()
                        .send(PacketDistributor.PLAYER.with { ctx.caster }, MsgColorizerUpdateAck(cap))
                }
            }
        }
    }
}
