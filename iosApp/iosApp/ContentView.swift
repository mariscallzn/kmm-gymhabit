import SwiftUI
import shared

struct ContentView: View {
    
    @StateObject var store: ObservableExerciseStore = ObservableExerciseStore(store: ExerciseStore())
    @SwiftUI.State var errorMessage: String?

	var body: some View {
        NavigationView{
            Form {
                AddExerciseView(store: store)
                ExerciseListView(store: store)
            }
            .navigationTitle("GymHabit")
        }
	}
}

struct AddExerciseView: View {
    @ObservedObject var store: ObservableExerciseStore
    @SwiftUI.State private var name = ""
    @SwiftUI.State private var muscle: UiMuscles = UiMuscles(id: 1, name: "Chest")
    @SwiftUI.State private var equipment: UiEquipment = UiEquipment(id: 1, name: "Dumbbell")
    
    @SwiftUI.State private var weight = 0.0
    
    var body: some View {
        Section {
            TextField("Exercise name", text: $name)
            
            Picker("Muscle", selection: $muscle){
                ForEach(store.state.muscles, id: \.self) { uiModel in
                    Text("\(uiModel.name)")
                }
            }
            
            Picker("Equipment", selection: $equipment){
                ForEach(store.state.equipments, id: \.self) { uiModel in
                    Text("\(uiModel.name)")
                }
            }
        
            Button("Add") {
                store.dispatch(ExerciseAction.Add(exercise: UiExercise(id: nil, name: name, muscles: [muscle], equipments: [equipment])))
            }
        }
            
    }
}

struct ExerciseListView: View {
    @ObservedObject var store: ObservableExerciseStore
    
    var body: some View {
        Section {
            ForEach (store.state.exercises, id: \.self) {exercise in
                Text("\(exercise.id ?? 0). \(exercise.name)")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
