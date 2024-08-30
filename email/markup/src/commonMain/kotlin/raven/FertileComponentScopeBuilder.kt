package raven

import raven.internal.FertileComponentScopeImpl

inline fun <C : FertileComponent> scopeOf(parent: C): ComponentScope<C> = FertileComponentScopeImpl(parent)