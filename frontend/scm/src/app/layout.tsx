"use client";
import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/Components/Navbar/Navbar";
import ClientOnly from "@/Components/ClientOnly/ClientOnly";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html data-theme="winter" lang="en">
        <body className={inter.className}>
        <ClientOnly>
            <Navbar />
        </ClientOnly>
        {children}
        </body>
        </html>
    );
}
