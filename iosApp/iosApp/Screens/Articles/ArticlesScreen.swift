//
//  ArticlesScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 7/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

@MainActor
class ArticlesViewModelWrapper: ObservableObject {
    
    @Published
    private(set) var articlesState: ArticlesState
    
    let articlesViewModel: ArticlesViewModel
    
    init(articlesViewModel: ArticlesViewModel? = nil) {
        let viewModel = articlesViewModel ?? ArticlesInjector().viewModel
        self.articlesState = viewModel.articlesState.value
        self.articlesViewModel = viewModel
    }
    
    func startObserving() async {
        for await articleS in articlesViewModel.articlesState {
            self.articlesState = articleS
        }
    }
}

struct ArticlesScreen: View {
    
    @StateObject
    private var viewModel: ArticlesViewModelWrapper
    
    init() {
        _viewModel = .init(wrappedValue: ArticlesViewModelWrapper())
    }
    
    var body: some View {
        VStack {
            if viewModel.articlesState.loading {
                ProgressView("Loading...")
            } else if !viewModel.articlesState.articles.isEmpty {
                makeArticleList(with: viewModel.articlesState.articles)
            } else if let errorMessage = viewModel.articlesState.error {
                Text(errorMessage)
            }
        }.task {
            await viewModel.startObserving()
        }
        .padding(.top, 40)
        .padding(.horizontal, 20)
        .navigationTitle("Articles")
    }
    
    private func makeArticleList(with articles: [Article]) -> some View {
        ScrollView(.vertical) {
            LazyVStack {
                ForEach(articles, id: \.self) { article in
                    ArticleRowView(with: article)
                }
            }
        }
    }
}



#Preview {
    ArticlesScreen()
}

extension Article: Identifiable {
    public var id: String {
        "\(title) \(imageURL)"
    }
}
