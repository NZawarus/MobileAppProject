package com.msoe.dndassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CombatCheatSheetFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val cheatSheetTitles = listOf("Move", "Action", "Bonus", "Free", "Reaction", "Hold")
    private val cheatSheetContent = listOf(
        """
        • Move up to your speed (Typically 30 feet for Medium Races)<br/>
        • Break up movement around actions<br/>
        &emsp;• Example: Dave would like to move 15 feet to help Alex with a check, then use the remainder of his movement to engage the enemy goblin.<br/>
        • Leaving melee range may provoke opportunity attacks<br/>
        &emsp;• Example: Dave is engaged in combat with a goblin, but attempts to move out of range. The goblin is able to make an attack roll against him.<br/>
        &emsp;• Opportunity attacks can be avoided by Disengaging (See Actions)
        """.trimIndent(),
        """
        • <b>Attack</b> - Roll to hit with a melee or ranged weapon, then roll for damage if successful<br/>
        • <b>Grapple</b> - A special melee attack that, if successful, restrains the target<br/>
        &emsp;• Target must be no more than one size category larger than you<br/>
        • <b>Shove</b> - Using an Attack action, you may shove a creature to either knock it prone or push it away from you. Instead of rolling to hit, make a contested Strength check with the target<br/>
        • <b>Cast a Spell</b> - Refer to spells for details on spells in particular<br/>
        &emsp;• <b>Spell Attack</b> - Roll to hit with a spell, then roll for damage if successful<br/>
        &emsp;• <b>Spell Save</b> - Enemy must roll to save against spell with the specified stat. Spell Save DC is determined by 10 + your primary spellcasting modifier<br/>
        • <b>Dash</b> - Move up to double your movement speed this turn<br/>
        • <b>Dodge</b> - Until the start of your next turn, any attack roll made against you will be made with disadvantage if you can see the attacker<br/>
        • <b>Disengage</b> - Disengage from combat with a creature. Your movement does not provoke opportunity attacks for the rest of the turn<br/>
        • <b>Hide</b> - Roll a dexterity (stealth) check in an attempt to hide<br/>
        • <b>Help</b> - Lend your aid to another creature in the completion of a task. The creature you aid gains advantage on the next ability check or attack roll made before the start of your next turn<br/>
        • <b>Ready an Action</b> - Use your action to prepare for a specified trigger later (See Hold Action)<br/>
        • <b>Use an Object</b> - When you must make use of an object<br/>
        &emsp;• Examples: Pulling a lever, sheathing/unsheathing sword, switching weapons, using a potion, etc.<br/>
        • <b>Search</b> - Make either a wisdom (perception) or intelligence (investigation) check to search for something: an object, hidden enemy, etc.
        """.trimIndent(),
        """
        • Only available if something allows it<br/>
        • Examples: Dave is a paladin. He would like to use his bonus action to use Lay on Hands to heal Alex.<br/>
        • You may only take one bonus action per round<br/>
        • Unless specified by the ability, you may choose when to take a bonus action on your turn
        """.trimIndent(),
        """
        • Can be done during movement or action<br/>
        • First object interaction per turn is free<br/>
        • Communication is possible as you take your turn<br/>
        • Other options include a variety of flourishes unrelated to your action and movement<br/>
        • Must be reasonable to achieve in one round of combat, or around 6 seconds)<br/>
        • Example: Dave, during his movement, unsheathes his greatsword, while yelling for Alex to flank the very fast goblin.
        """.trimIndent(),
        """
        • Used when something triggers a reaction<br/>
        • Examples: Dave provokes an opportunity attack from an enemy goblin. In response, Alex uses his reaction to cast Shield, increasing Dave's AC by 5. This causes the goblin to miss their attack, and Dave is able to move away<br/>
        • Only one reaction per round
        """.trimIndent(),
        """
        • Use your action to prepare for later<br/>
        • Choose a trigger + an action<br/>
        &emsp;• Example: If the goblin approaches me, I will dash away towards the party<br/>
        • When it happens, use your reaction to act
        """.trimIndent()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.combat_cheat_sheet, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        val adapter = CheatSheetPagerAdapter(this, cheatSheetTitles, cheatSheetContent)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = cheatSheetTitles[position]
        }.attach()

        return view
    }
}
