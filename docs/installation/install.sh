#!/bin/sh

### Constants
RELEASES_URL=https://api.github.com/repos/raynigon/raydio/releases/latest

### Logging
log_debug () {
    echo "[DEBUG] $1"
}

log_info () {
    echo "[INFO] $1"
}

log_warn () {
    echo "[WARN] $1"
}

log_error () {
    echo "[ERROR] $1"
}

### Helper
abort () {
    log_error "Abort Installation"
    exit $1
}

is_installed () { 
    which "$1" | grep -o "$1" > /dev/null &&  return 0 || return 1 
}

check_tools () {
    if is_installed "python3"; then
        log_debug "python3 is installed"
    else
        log_error "python3 is not installed, unable to proceed Installation"
        abort 9
    fi

    if is_installed "curl"; then
        log_debug "curl is installed"
    else
        log_error "curl is not installed, unable to proceed Installation"
        abort 9
    fi

    if is_installed "mktemp"; then
        log_debug "mktemp is installed"
    else
        log_error "mktemp is not installed, unable to proceed Installation"
        abort 9
    fi
}

### Installation
download () {
    EXTENSION=$1
    RELEASES_JSON=$(curl -s $RELEASES_URL)
    DOWNLOAD_URL=$(echo $RELEASES_JSON | python3 -c "
import sys, json; 
document = json.load(sys.stdin)
if 'assets' not in document.keys():
    print('<--NOT_FOUND-->')
    exit(0)
assets = list(filter(lambda x: x['name'].endswith('.$EXTENSION'), document['assets']))
if len(assets) < 1:
    print('<--NOT_FOUND-->')
    exit(0)
print(assets[0]['url'])
")
    if [[ $DOWNLOAD_URL == *"<--NOT_FOUND-->"* ]]; then
        log_error "Unable to find Download URL"
        abort 1
    fi
    TMP_DEB_FILE=$(mktemp)
    log_info "Downloading Package..."
    curl -s -H "Accept: application/octet-stream" --output $TMP_DEB_FILE $DOWNLOAD_URL
    RESULT=$?
    if [[ $RESULT -ne 0 ]]; then
        log_error "Unable Download Package"
        abort 2
    fi
    log_info "Finished Download"
    export PACKAGE_FILE=$TMP_DEB_FILE
    return 0
}

install_deb () {
    DEB_FILE=$1
    log_info "Starting Package Installation"
    log_info "Superuser required for Installation"
    sudo apt install -y $DEB_FILE
    RESULT=$?
    if [[ $RESULT -ne 0 ]]; then
        log_error "Unable to Install Package"
        exit 3
    fi
}

install_rpm_with_yum () {
    RPM_FILE=$1
    log_info "Starting Package Installation"
    log_info "Superuser required for Installation"
    sudo yum localinstall $RPM_FILE
    RESULT=$?
    if [[ $RESULT -ne 0 ]]; then
        log_error "Unable to Install Package"
        abort 3
    fi
}

install_rpm_with_dnf () {
    RPM_FILE=$1
    log_info "Starting Package Installation"
    log_info "Superuser required for Installation"
    sudo dnf localinstall $RPM_FILE
    RESULT=$?
    if [[ $RESULT -ne 0 ]]; then
        log_error "Unable to Install Package"
        abort 3
    fi
}

install_rpm_plain () {
    RPM_FILE=$1
    log_info "Starting Package Installation"
    log_info "Superuser required for Installation"
    sudo rpm –i $RPM_FILE
    RESULT=$?
    if [[ $RESULT -ne 0 ]]; then
        log_error "Unable to Install Package"
        abort 3
    fi
}

### Script Logic

check_tools

if is_installed "apt"; then
    log_info "Starting Installation with 'apt'"
    download "deb"
    install_deb $PACKAGE_FILE
elif is_installed "yum"; then
    log_info "Starting Installation with 'yum'"
    download "rpm"
    install_rpm_with_yum $PACKAGE_FILE
elif is_installed "dnf"; then
    log_info "Starting Installation with 'dnf'"
    download "rpm"
    install_rpm_with_dnf $PACKAGE_FILE
elif is_installed "rpm"; then
    log_info "Starting Installation with 'rpm'"
    download "rpm"
    install_rpm_plain $PACKAGE_FILE
else
    log_error "No Package Manager was found, please install the package manually"
    abort 4
fi
log_info "Rayd.io was installed successfully"

