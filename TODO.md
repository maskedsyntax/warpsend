# WarpSend Android TODO

Scope: Android-only MVP built with Kotlin, Jetpack Compose, Material 3, MVVM, Clean Architecture, Hilt, Ktor, Room, coroutines, Flow, kotlinx.serialization, ZXing, and local-network transfer primitives.

## 0. Project Baseline

- [x] Create Android project structure with Gradle, Kotlin, Compose, and an `app` module.
- [x] Configure package name, app name, launcher icon placeholders, min SDK, target SDK, and Android manifest.
- [x] Add core dependencies: Compose Material 3, Navigation Compose, Hilt, Room, Ktor, kotlinx.serialization, ZXing, coroutines, lifecycle, and test libraries.
- [x] Add Android-oriented `.gitignore` entries for Gradle, IDE, build outputs, and local SDK files.
- [x] Set up build variants and basic CI-ready commands for assemble, lint, and tests.

## 1. App Architecture

- [x] Define Clean Architecture layers: `data`, `domain`, `presentation`, and `core`.
- [x] Create MVVM screen state pattern using immutable UI state and one-shot UI events.
- [x] Configure Hilt modules for repositories, database, networking, dispatchers, and transfer services.
- [ ] Establish app navigation for Home, Send, Receive, Pairing, Transfer Detail, History, Settings, and Permissions.
- [ ] Add central permission handling for nearby Wi-Fi/network state, notifications, file access, camera, and foreground service requirements.

## 2. Core UX

- [x] Build Material 3 theme, typography, color system, and app shell.
- [x] Implement Home screen with send, receive, nearby devices, active transfers, and recent history.
- [ ] Implement file picker flow for images, videos, audio, PDFs, ZIPs, APKs, and documents.
- [ ] Implement receive mode screen with device visibility status and pairing entry points.
- [ ] Implement transfer progress UI showing percentage, speed, ETA, transferred bytes, total size, and current file.
- [ ] Implement transfer history UI with sent/received status, timestamp, device name, and file metadata.

## 3. Device Discovery

- [ ] Define `Device` domain model and discovery repository contract.
- [ ] Implement Android NSD service registration for the local WarpSend endpoint.
- [ ] Implement NSD discovery for nearby WarpSend devices.
- [ ] Add UDP broadcast discovery fallback.
- [ ] Normalize discovered device records across NSD, mDNS-compatible naming, and UDP responses.
- [ ] Add timeout, duplicate filtering, refresh, and stale-device cleanup behavior.

## 4. QR Pairing

- [ ] Define pairing payload schema with device ID, display name, IP/port, protocol version, and public handshake metadata.
- [ ] Generate QR code using ZXing for receiver pairing.
- [ ] Add camera scanner flow for QR pairing.
- [ ] Validate scanned payloads and create a trusted known-device record.
- [ ] Handle invalid, expired, incompatible, and duplicate pairing payloads.

## 5. Local Transfer Engine

- [ ] Define transfer session, file manifest, chunk, checksum, progress, and status domain models.
- [ ] Implement sender-side Ktor local HTTP server or endpoint exposure for chunked file transfer.
- [ ] Implement receiver-side Ktor client for chunk download/upload coordination.
- [ ] Support multi-file transfer manifests.
- [ ] Persist transfer session state so interrupted transfers can resume.
- [ ] Use content URIs safely through Android storage APIs without assuming direct filesystem paths.
- [ ] Add foreground service for long-running transfers.
- [ ] Add notification progress for active transfers.

## 6. Large File And Resume Support

- [ ] Choose chunk size policy and document tradeoffs.
- [ ] Store per-chunk completion state in Room.
- [ ] Implement checksum validation for chunks and final files.
- [ ] Resume after app restart, Wi-Fi interruption, or temporary disconnect.
- [ ] Add retry policy with backoff and user-visible failure states.
- [ ] Validate support path for files larger than 10 GB.

## 7. Persistence

- [x] Create Room entities for known devices, transfer history, transfer sessions, files, chunks, and settings.
- [x] Add DAOs and migrations policy.
- [x] Map Room entities to domain models.
- [x] Implement repositories for devices, transfers, history, and settings.
- [ ] Add cleanup policy for completed sessions and old temporary chunks.

## 8. Security And Privacy

- [ ] Generate stable local device ID without requiring accounts.
- [ ] Add encrypted pairing or handshake plan before exposing production builds.
- [ ] Add AES-256 encryption design for transfer payloads or session keys.
- [ ] Avoid analytics and unnecessary data collection in MVP.
- [ ] Document local-network threat model and MVP limitations.

## 9. Android Emulator And Device Testing

- [ ] Verify app launch on Android emulator.
- [ ] Verify permissions and file picker behavior on emulator.
- [ ] Test discovery with two emulator/device instances where networking permits it.
- [ ] Test QR pairing with generated QR and scanner flow.
- [ ] Test small file transfer.
- [ ] Test large file transfer and interrupted resume behavior.
- [ ] Test background transfer behavior with foreground service notification.

## 10. MVP Acceptance Criteria

- [ ] User can send supported file types to a paired nearby Android device.
- [ ] User can receive supported file types from a paired nearby Android device.
- [ ] Transfers show progress, speed, ETA, percentage, and file size.
- [ ] Sent and received transfers appear in history.
- [ ] Transfer sessions persist and can resume after interruption.
- [ ] Core transfer flow works without login, ads, or internet dependency.

## Deferred Beyond Android MVP

- [ ] Desktop clients for Windows, macOS, and Linux.
- [ ] Clipboard sync.
- [ ] Link sharing.
- [ ] Text snippets.
- [ ] Device migration tools.
- [ ] Auto sync and scheduled backups.
- [ ] Smart compression.
- [ ] Broadcast mode.
- [ ] Temporary local sharing links.
- [ ] Pro subscription and team plan features.
