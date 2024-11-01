import NextAuth, {NextAuthOptions} from "next-auth";
import DiscordProvider from "next-auth/providers/discord";

export const authOptions: NextAuthOptions = {
    providers: [
        DiscordProvider({
            clientId: process.env.DISCORD_CLIENT_ID!,
            clientSecret: process.env.DISCORD_CLIENT_SECRET!,
            authorization: {
                params: {
                    scope: "identify guilds"
                }
            }
        }),
    ],
    callbacks: {
        async jwt({token, account}) {
            if (account) {
                token.accessToken = account.access_token;
            }
            return token;
        },
        async session({session, token}) {
            session.accessToken = token.accessToken;
            return session;
        },
    },
    pages: {
        signIn: "/auth/login",
    },
}

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST };