import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      searchCandidates(req, res);
      break;
    default:
      break;
  }
}

async function searchCandidates(req, res) {
  try {
    const response = await http(req, res).get("/candidates/search", {
      params: req.query,
    });
    res.status(response.status).json(response.data);
  } catch (error) {
    res.status(400).json({});
  }
}
