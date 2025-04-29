import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        iOSKoinLoader.shared.doInit()
    }
    
	var body: some Scene {
		WindowGroup {
			AppTabsView()
		}
	}
}
