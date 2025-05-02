//
//  ArticlesScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 7/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ArticlesScreen: View {
    
    @StateObject
    private var viewModel: ArticlesViewModelWrapper
    
    init() {
        _viewModel = .init(wrappedValue: ArticlesViewModelWrapper())
    }
    
    var body: some View {
        VStack {
            if viewModel.contentState.loading {
                ProgressView("Loading...")
            } else if !viewModel.contentState.articles.isEmpty {
                makeArticleList(with: viewModel.contentState.articles)
            } else if let errorMessage = viewModel.contentState.error {
                Text(errorMessage)
            }
        }.task {
            await viewModel.startObservingChanges()
        }
        .padding(.top, 40)
        .padding(.horizontal, 20)
        .navigationTitle("Articles")
        .navigationBarTitleDisplayMode(.inline)
    }
    
    private func makeArticleList(with articles: [Article]) -> some View {
        ScrollView(.vertical) {
            LazyVStack {
                ForEach(articles, id: \.self) { article in
                    ArticleRowView(with: article)
                }
            }
        }.refreshable {
            await viewModel.onRefreshContent()
        }
    }
}

#Preview {
    ArticlesScreen()
}

extension Article: @retroactive Identifiable {
    public var id: String {
        "\(title) \(imageURL)"
    }
}
