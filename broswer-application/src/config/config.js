const PROTOCAL = window && window.location && window.location.protocol;
const HOST_NAME = window && window.location && window.location.hostname;
const PORT = 3001;

const baseApiPath = `${PROTOCAL}//${HOST_NAME}:${PORT}`;
const authorizedApiPath = `${baseApiPath}/api`;

export default {
  baseApiPath,
  authorizedApiPath
}
