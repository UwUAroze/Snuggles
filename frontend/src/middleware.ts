import { withAuth } from "next-auth/middleware";

export default withAuth({
    pages: {
        signIn: '/auth/login',
    },
});

export const config = {
    matcher: [
        // protect all routes except public ones
        "/((?!api|_next/static|_next/image|favicon.ico|auth/signin|/).*)",
    ],
};