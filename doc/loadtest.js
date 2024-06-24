// https://k6.io/docs/get-started/running-k6/

import http from "k6/http";
import { check, sleep } from 'k6';

export const options = {
    vus: 10, // Number of virtual users
    duration: '5s', // Duration of the test
};

const baseUrl = 'http://localhost:50900';

export default function () {
    checkResponse(http.get(`${baseUrl}/callees/sayMyName?name=Heisenberg`));
    checkResponse(http.get(`${baseUrl}/callees/sayMyOtherName/Dr.%20Dre`));
}

function checkResponse(response) {
    check(response, {
        'status is 200': (r) => r.status === 200,
    });
    if (response.status !== 200) {
        console.error(`Unexpected status for request: ${response.status}`);
    }
}
