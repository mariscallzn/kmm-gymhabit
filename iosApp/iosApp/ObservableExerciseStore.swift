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
    @Published public var event: ExerciseEvent?
    
    let store: ExerciseStore
    
    var stateWathcer: Closeable?
    var eventWatcher: Closeable?
    
    init(store: ExerciseStore){
        self.store = store
        dispatch(ExerciseAction.InitialLoad())
        stateWathcer = self.store.watchState().watch {[weak self] state in
            self?.state = state
        }
        eventWatcher = self.store.watchEvent().watch {[weak self] event in
            self?.event = event
        }
    }
    
    public func dispatch(_ action: ExerciseAction){
       store.dispatch(action: action)
    }
    
    deinit {
        stateWathcer?.close()
        eventWatcher?.close()
    }
}
