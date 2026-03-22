<h1>API Information</h1>
<p>Live API Documentation</p>
<p>The API is fully documented and interactive via Swagger UI. You can test the endpoints, view request schemas, and explore the API live:
</p>
<h3>Explore Songs Wallah API Documentation</h3>
<p><a href = "https://music-apis-eokm.onrender.com/swagger-ui/index.html">Swagger</a></p>
<h3>Base URL</h3>
<p>https://music-apis-eokm.onrender.com</p>
<br>
<p>**Note:** Since the app is hosted on Render's free tier, the first request might take 30-60 seconds to "wake up" the server.</p>

<h1>Information</h1>

<h2>1) Identity and Security (Core System)</h2>

<p>
  Here, security is not viewed as an add-on; rather, it serves as the foundation upon which the rest of the application is built.
</p>

<p>
  JWT-based stateless sessions, which eliminate the need for server-side session storage and scale smoothly as traffic increases,
  are used to handle authentication. After logging in, tokens are generated and verified for each protected request.
</p>

<p>
  SendGrid's email-based OTP validation is used to enforce user verification. Fake or partially created users are eliminated
  early in the lifecycle because accounts cannot be used until verification is finished.
</p>

<p>
  Expired OTPs are automatically eliminated using native SQL cleanup queries to avoid OTP clutter and needless table growth.
  This eliminates the need for manual intervention or cron jobs, keeping the verification table lean.
</p>

<h2>2) Finding & Searching for Music</h2>

<p>
  Instead of using a flat list, the song system is designed to meet real-world discovery needs.
</p>

<p>
  Several criteria can be used to filter songs:
</p>

<ul>
  <li>Name of the artist</li>
  <li>Language</li>
  <li>Category or genre</li>
</ul>

<p>
  Combining these filters makes it simple to refine results without using complex query logic.
</p>

<p>
  The API returns streaming-ready data without the need for extra lookups or transformations because each song stores all of its
  metadata, including duration, ratings, and cloud storage URLs.
</p>

<p>
  For complex filters, the repository layer uses native SQL, which provides consistent performance as the dataset expands.
</p>

<h2>3) Admin Tools</h2>

<p>
  Administrative functionality is deliberately isolated from standard user workflows.
</p>

<p>
  Admins are provided with dedicated endpoints that allow them to:
</p>

<ul>
  <li>
    Upload multiple songs in a single batch, making large library updates efficient
  </li>
  <li>
    Update song metadata such as ratings, URLs, and categories without requiring re-uploads
  </li>
  <li>
    View user profiles and process account deletions through controlled request objects
  </li>
</ul>

<p>
  Admin and user operations do not share endpoint logic. This separation prevents permission
  leakage and keeps responsibilities clearly defined within the system.
</p>
<h2>4) Performance &amp; Reliability</h2>

<p>
  Performance and reliability decisions in the system are intentional rather than abstract
  optimizations.
</p>

<p>
  All request and response payloads are implemented using <strong>Java Records</strong>, which
  enforces immutability and minimizes unintended side effects across service boundaries.
</p>

<p>
  Error handling is centralized through a global exception handler. Scenarios such as missing
  songs, invalid OTPs, or unauthorized access return clear, structured error responses instead of
  generic server failures.
</p>

<p>
  In cases where ORM-based queries became inefficient or difficult to reason about,
  <strong>native SQL was used deliberately</strong>. This improves execution performance and query
  readability, with the known trade-off of reduced portability—an acceptable compromise for this
  application.
</p>

<p>
  Overall, the system is designed to scale cleanly without unnecessary architectural complexity.
</p>

<h1>Database Architecture</h1>

<p>
  The database design for <strong>Songs Wallah</strong> is built around five core tables, each with
  a clearly defined responsibility. The schema avoids unnecessary coupling while still supporting
  performance-heavy queries.
</p>

<h3>1. Users Table (<code>users</code>)</h3>

<p>
  This table acts as the system’s primary identity source.
</p>

<ul>
  <li><strong>Primary Key:</strong> <code>id</code> (Long)</li>
  <li>
    <strong>Public Identifier:</strong> <code>public_id</code> (UUID), used in API responses to
    avoid exposing internal IDs
  </li>
  <li>
    <strong>Fields:</strong> Email, encrypted password, first name, last name, and age
  </li>
  <li>
    <strong>Verification:</strong> <code>email_verification</code> flag to control access
  </li>
</ul>

<h3>2. Songs Table (<code>song_details</code>)</h3>

<p>
  This table represents the global music catalog.
</p>

<ul>
  <li>
    <strong>Indexing:</strong> Optimized for filtering by artist name, category, and language using
    native SQL
  </li>
  <li>
    <strong>Metadata:</strong> Cloud URL, duration, rating, and public UUID
  </li>
</ul>

<h3>3. Playlists Table (<code>playlist</code>)</h3>

<p>
  This table manages user-created collections and their visibility.
</p>

<ul>
  <li><strong>Ownership:</strong> Linked to users via <code>owner_id</code></li>
  <li>
    <strong>Privacy:</strong> Uses <code>accessibility</code> (PUBLIC / PRIVATE) to control
    visibility
  </li>
  <li>
    <strong>Song Mapping:</strong> Many-to-many relationship via a join table
    (e.g., <code>playlist_songs</code>)
  </li>
</ul>

<h3>4. Favorites Table (<code>favorite</code>)</h3>

<p>
  This table handles personalized user preferences.
</p>

<ul>
  <li>
    <strong>Priority-Based Sorting:</strong> Includes a <code>priority</code> column (High, Medium,
    Low)
  </li>
  <li>
    <strong>Per


