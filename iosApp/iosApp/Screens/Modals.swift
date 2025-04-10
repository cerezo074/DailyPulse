//
//  Modals.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct ModalScreen1: View {

    @ObservedObject
    var settingsCoordinator: SettingsCoordinator
    
    var body: some View {
        NavigationStack(path: $settingsCoordinator.navigationStack) {
            settingsCoordinator.build(screen: .shareFeedback)
                .navigationDestination(for: SettingsCoordinator.Screen.self) { articlesScreen in
                    settingsCoordinator.build(screen: articlesScreen)
                }
        }
    }
}
