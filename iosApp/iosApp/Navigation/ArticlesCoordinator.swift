//
//  ArticlesCoordinator.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

class ArticlesCoordinator: ObservableObject {
    
    enum Screen: Hashable {
        case home
        case articles
        case detail
    }
    
    @Published
    var navigationStack: NavigationPath = .init()
    
    func start() -> some View {
        build(screen: .home)
    }
    
    func navigate(to screen: Screen) {
        navigationStack.append(screen)
    }
    
    @ViewBuilder
    func build(screen: Screen) -> some View {
        switch screen {
        case .home:
            ArticlesHomeScreen(articlesCoordinator: self)
        case .articles:
            ArticlesScreen()
        case .detail:
            VStack {
                Text("Detail is still under construction")
            }
        }
    }
}
