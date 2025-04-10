//
//  ArticleRowView.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 7/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ArticleRowView: View {
    
    let article: Article
    
    init(with article: Article) {
        self.article = article
    }
    
    var body: some View {
        VStack {
            image
            informationContainer
        }
    }
    
    @ViewBuilder
    private var image: some View {
        if let articleImageURL = URL(string: article.imageURL) {
            AsyncImage(url: articleImageURL) { image in
                image.resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                ZStack {
                    Rectangle().fill(Color.gray.opacity(0.5))
                        .frame(height: 100)
                        .aspectRatio(contentMode: .fill)
                    Text("Downloading Image...")
                }
            }
        } else {
            ZStack {
                Rectangle().fill(Color.red.opacity(0.8))
                    .frame(height: 100)
                    .aspectRatio(contentMode: .fill)
                Text("Failed to load Image :(")
            }
        }
    }
    
    private var informationContainer: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(attributedContent)
                .foregroundColor(.black)
                .fixedSize(horizontal: false, vertical: true)
            HStack {
                Spacer()
                Text(article.date)
                    .foregroundStyle(.secondary)
                    .font(.caption)
            }
        }
    }
    
    private var attributedContent: AttributedString {
        var fullText = AttributedString("\(article.title)\n\n\(article.desc)")
        
        if let titleRange = fullText.range(of: article.title) {
            fullText[titleRange].font = .largeTitle.weight(.semibold)
        }
        
        if let descRange = fullText.range(of: article.desc) {
            fullText[descRange].font = .body
        }
        
        return fullText
    }
    
}

#Preview {
    ArticleRowView(
        with: Article(
            title: "Stock market today: Live updates - CNBC",
            desc: "Futures were higher in premarket trading as Wall Street tried to regain its footing.",
            date: "2023-11-09",
            imageURL: "https://image.cnbcfm.com/api/v1/image/107326078-1698758530118-gettyimages-1765623456-wall26362_igj6ehhp.jpeg?v=1698758587&w=1920&h=1080"
        )
    ).frame(width: 375, height: 200)
}
