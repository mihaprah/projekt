// src/app/not-found.tsx

import Link from 'next/link';

export default function NotFound() {
    return (
        <div className="flex flex-col justify-center items-center h-screen">
            <div className="text-center">
                <h1 className="text-4xl font-bold mb-4">404 - Page Not Found</h1>
                <p className="text-lg mb-4">Sorry, the page you are looking for does not exist.</p>
                <Link href="/">
                    <span className="text-blue-500 underline">Go back to the homepage</span>
                </Link>
            </div>
        </div>
    );
}
