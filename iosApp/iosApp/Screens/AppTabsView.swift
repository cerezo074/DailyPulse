import SwiftUI

struct AppTabsView: View {
    
    @StateObject
    var articlesCoordinator = ArticlesCoordinator()
    @StateObject
    var settingsCoordinator = SettingsCoordinator()

	var body: some View {
        TabView {
            articlesCoordinator.start()
                .tabItem {
                    Label("Articles", systemImage: "house")
                }
            settingsCoordinator.start()
                .tabItem {
                    Label("Settings", systemImage: "gearshape")
                }
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		AppTabsView()
	}
}
