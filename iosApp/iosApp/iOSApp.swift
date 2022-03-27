import SwiftUI
import shared

@main
class iOSApp: App {
    
    let core = GymHabitCore(databaseDriverFactory: DatabaseDriverFactory())
    
    required init() {
        core.startAppFramework()
        print("Andy: \(NSHomeDirectory())")
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView()
		}
	}
}
