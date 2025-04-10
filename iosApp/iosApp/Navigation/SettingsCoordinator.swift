//
//  SettingsCoordinator.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

class SettingsCoordinator: ObservableObject {
    
    enum Screen: Hashable {
        case home
        case menu
        case profile
        case about
        case shareFeedback
    }
    
    enum ModalScreen: String, Identifiable {
        case shareFeedback
        
        var id: String {
            self.rawValue
        }
    }
    
    @Published
    var navigationStack: NavigationPath = .init()
    @Published
    var modalItem: ModalScreen? = nil
    
    func start() -> some View {
        build(screen: .home)
    }
    
    func navigate(to screen: Screen) {
        navigationStack.append(screen)
    }
    
    func openModal(screen: ModalScreen) {
        self.modalItem = screen
    }
    
    @ViewBuilder
    func build(screen: Screen) -> some View {
        switch screen {
        case .home:
            SettingsHomeScreen(settingsCoordinator: self)
        case .profile:
            VStack {
                Text("Profle is still under construction")
            }
        case .about:
            AboutScreen()
        case .menu:
            SettingsMenuView(coordinator: self)
        case .shareFeedback:
            VStack {
                Text("Share feedback is still under construction")
            }
        }
    }
    
    @ViewBuilder
    func build(modalScreen: ModalScreen) -> some View {
        switch modalScreen {
        case .shareFeedback:
            ModalScreen1(settingsCoordinator: self)
        }
    }
}
