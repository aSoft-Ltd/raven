package raven.internal

import raven.FertileComponent
import raven.ComponentScope

@PublishedApi
internal class FertileComponentScopeImpl<C : FertileComponent>(
    override val parent: C
) : ComponentScope<C>