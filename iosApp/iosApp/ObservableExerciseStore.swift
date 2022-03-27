//
//  ObservableExerciseStore.swift
//  iosApp
//
//  Created by Andres Mariscal on 3/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class ObservableExerciseStore: ObservableObject {
    @Published public var state: ExerciseState = ExerciseState(exercises: [UiExercise](), muscles: [UiMuscles](), equipments: [UiEquipment]())
    @Published public var effect: ExerciseEffect?
    
    let store: ExerciseStore
    
    var stateWathcer: Closeable?
    var sideEffectWatcher: Closeable?
    
    init(store: ExerciseStore){
        self.store = store
        dispatch(ExerciseAction.InitialLoad())
        stateWathcer = self.store.watchState().watch {[weak self] state in
            self?.state = state
        }
        sideEffectWatcher = self.store.watchSideEffect().watch {[weak self] sideEffect in
            self?.effect = sideEffect
        }
    }
    
    public func dispatch(_ action: ExerciseAction){
       store.dispatch(action: action)
    }
    
    deinit {
        stateWathcer?.close()
        sideEffectWatcher?.close()
    }
}
