//
//  SettingsHomeScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SettingsHomeScreen: View {
    @ObservedObject
    var settingsCoordinator: SettingsCoordinator
    
    var body: some View {
        NavigationStack(path: $settingsCoordinator.navigationStack) {
            settingsCoordinator.build(screen: .menu)
                .sheet(item: $settingsCoordinator.modalItem) { modalItem in
                    settingsCoordinator.build(modalScreen: modalItem)
                }
                .navigationDestination(for: SettingsCoordinator.Screen.self) { settingsScreen in
                    settingsCoordinator.build(screen: settingsScreen)
                }
        }
    }
}
